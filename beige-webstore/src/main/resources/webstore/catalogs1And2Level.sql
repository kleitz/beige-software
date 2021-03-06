select CAT1LID,  CAT1LNAME, CAT1HS, SUBCATALOG as CAT2LID, CATALOGGS.ITSNAME as CAT2LNAME, HASSUBCATALOGS as CAT2HS
from
  (
    select ITSID as CAT1LID, ITSNAME as CAT1LNAME, ITSINDEX as CAT1LINDEX, HASSUBCATALOGS as CAT1HS
    from CATALOGGS
    left join SUBCATALOGSCATALOGSGS on SUBCATALOGSCATALOGSGS.SUBCATALOG=CATALOGGS.ITSID
    where SUBCATALOG is null and ISINMENU=1
  ) as CAT1L
left join SUBCATALOGSCATALOGSGS on SUBCATALOGSCATALOGSGS.ITSCATALOG=CAT1L.CAT1LID
left join CATALOGGS on CATALOGGS.ITSID=SUBCATALOG
where ISINMENU is null or ISINMENU=1
order by CAT1LINDEX, ITSINDEX;
