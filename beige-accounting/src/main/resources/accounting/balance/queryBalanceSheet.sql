select ACC as ACCID, ITSNUMBER, ITSTYPE, ACCOUNTNAME,
case when NORMALBALANCETYPE=0 then ALL_RECORDS.DEBIT-ALL_RECORDS.CREDIT
else 0 end as DEBIT,
case when NORMALBALANCETYPE=1 then ALL_RECORDS.CREDIT-ALL_RECORDS.DEBIT
else 0 end as CREDIT
from
  (
    select ACC, ITSNUMBER, ITSTYPE, NORMALBALANCETYPE, ACCOUNTNAME, sum(DEBIT) as DEBIT, sum(CREDIT) as CREDIT
    from 
      (
        select ITSACCOUNT as ACC, ITSNUMBER, ITSTYPE, NORMALBALANCETYPE, ACCOUNT.ITSNAME as ACCOUNTNAME, 
        case when ACCOUNT.NORMALBALANCETYPE=0 then ITSBALANCE
        else 0 end as DEBIT,
        case when ACCOUNT.NORMALBALANCETYPE=1 then ITSBALANCE
        else 0 end as CREDIT
        from  BALANCEAT
        join ACCOUNT on BALANCEAT.ITSACCOUNT=ACCOUNT.ITSID 
        where ITSTYPE<3 and ITSDATE=:DATE1
        
        union all

        select ACCDEBIT as ACC, ITSNUMBER, ITSTYPE, NORMALBALANCETYPE, ACCOUNT.ITSNAME as ACCOUNTNAME, sum(DEBIT) as DEBIT, 0.00 as CREDIT
        from  ACCOUNTINGENTRY 
        join ACCOUNT on ACCOUNTINGENTRY.ACCDEBIT=ACCOUNT.ITSID 
        where ITSTYPE<3 and ITSDATE>=:DATE1 and ITSDATE<=:DATE2
        group by ACC, ITSNUMBER, ITSTYPE, NORMALBALANCETYPE, ACCOUNT.ITSNAME
        
        union all
        
        select ACCCREDIT as ACC, ITSNUMBER, ITSTYPE, NORMALBALANCETYPE, ACCOUNT.ITSNAME as ACCOUNTNAME, 0 as DEBIT, sum(CREDIT) as CREDIT
        from  ACCOUNTINGENTRY 
        join ACCOUNT on ACCOUNTINGENTRY.ACCCREDIT=ACCOUNT.ITSID 
        where ITSTYPE<3 and ITSDATE>=:DATE1 and ITSDATE<=:DATE2
        group by ACC, ITSTYPE, NORMALBALANCETYPE, ACCOUNT.ITSNAME
      ) as UNION_RECORDS
    group by ACC, ITSNUMBER, ITSTYPE, NORMALBALANCETYPE, ACCOUNTNAME
  ) as ALL_RECORDS
order by ITSNUMBER;
