package org.beigesoft.orm.processor;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;

import org.beigesoft.model.IRequestData;
import org.beigesoft.persistable.EmailMsg;
import org.beigesoft.persistable.EmailStringProperty;
import org.beigesoft.persistable.EmailIntegerProperty;
import org.beigesoft.persistable.Erecipient;
import org.beigesoft.persistable.Eattachment;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.IMailSender;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Service that save email message into DB and send it when
 * it's requested.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcEmailMsgSave<RS>
  implements IEntityProcessor<EmailMsg, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Email sender.</p>
   **/
  private IMailSender emailSender;

  /**
   * <p>Process pEntity request.</p>
   * @param pAddParam additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final EmailMsg process(
    final Map<String, Object> pAddParam, final EmailMsg pEntity,
      final IRequestData pRequestData) throws Exception {
    if (pEntity.getIsNew()) {
      this.srvOrm.insertEntity(pAddParam, pEntity);
      pEntity.setIsNew(false);
    } else {
      if (pEntity.getIsSent()) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "can_not_change_sent_email");
      }
      boolean isNeedSend = false;
      if (pRequestData.getParameter("actionAdd") != null) {
        //send from form/list
        isNeedSend = true;
      }
      if (isNeedSend) {
        Erecipient erec = new Erecipient();
        erec.setItsOwner(pEntity);
        pEntity.setErecipients(getSrvOrm()
          .retrieveListForField(pAddParam, erec, "itsOwner"));
        if (pEntity.getErecipients().size() == 0) {
          throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
            "choose_recipient");
        }
        pEntity.setEmailConnect(this.srvOrm.retrieveEntity(pAddParam,
          pEntity.getEmailConnect()));
        EmailStringProperty emStrProp = new EmailStringProperty();
        emStrProp.setItsOwner(pEntity.getEmailConnect());
        pEntity.getEmailConnect().setStringProperties(getSrvOrm()
          .retrieveListForField(pAddParam, emStrProp, "itsOwner"));
        EmailIntegerProperty emIntProp = new EmailIntegerProperty();
        emIntProp.setItsOwner(pEntity.getEmailConnect());
        pEntity.getEmailConnect().setIntegerProperties(getSrvOrm()
          .retrieveListForField(pAddParam, emIntProp, "itsOwner"));
        Eattachment eattach = new Eattachment();
        eattach.setItsOwner(pEntity);
        pEntity.setEattachments(getSrvOrm()
          .retrieveListForField(pAddParam, eattach, "itsOwner"));
        getEmailSender().openConnection(pAddParam, pEntity.getEmailConnect());
        getEmailSender().sendEmail(pAddParam, pEntity);
        getEmailSender().closeConnection(pAddParam, pEntity.getEmailConnect());
        pEntity.setIsSent(true);
      }
      this.srvOrm.updateEntity(pAddParam, pEntity);
    }
    return pEntity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for emailSender.</p>
   * @return IMailSender
   **/
  public final IMailSender getEmailSender() {
    return this.emailSender;
  }

  /**
   * <p>Setter for emailSender.</p>
   * @param pEmailSender reference
   **/
  public final void setEmailSender(final IMailSender pEmailSender) {
    this.emailSender = pEmailSender;
  }
}
