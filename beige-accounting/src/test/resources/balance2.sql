/*Jurnal entries sources:*/
/*Debits:*/
select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
'INVENTORY' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(PURCHASEINVOICELINE.ITSTOTAL) as DEBIT,
null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
from PURCHASEINVOICELINE
join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICELINE.ITSOWNER
join INVITEM on INVITEM.ITSID = PURCHASEINVOICELINE.INVITEM
join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
where REVERSEDLINEID is null :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

union all

select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
'SALES_TAX_RECIVABLE' as ACC_DEBIT, TAX.ITSNAME as SUBACC_DEBIT, sum(PURCHASEINVOICETAXLINE.ITSTOTAL) as DEBIT,
null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
from PURCHASEINVOICETAXLINE 
join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICETAXLINE.ITSOWNER
join TAX on TAX.ITSID = PURCHASEINVOICETAXLINE.ITSTAX 
where TAX.DUEMETHOD = 0 :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

union all

select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
'ACC_RECIVABLE' as ACC_DEBIT, DEBTORCREDITOR.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.TOTALWITHTAXES ) as DEBIT,
null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
from SALESINVOICELINE
join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
join DEBTORCREDITOR on DEBTORCREDITOR.ITSID = SALESINVOICE.CUSTOMER 
where REVERSEDLINEID is null :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

/*Debits-Credits:*/
select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
'COGSKNC' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as DEBIT,
'INVENTORY' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as CREDIT
from SALESINVOICELINE
join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
where REVERSEDLINEID is null and INVITEM.KNOWNCOST is not null :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

/*Credits:*/
select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
'ACC_PAYABLE' as ACC_CREDIT, DEBTORCREDITOR.ITSNAME as SUBACC_CREDIT, sum(PURCHASEINVOICELINE.TOTALWITHTAXES ) as CREDIT
from PURCHASEINVOICELINE
join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICELINE.ITSOWNER
join DEBTORCREDITOR on DEBTORCREDITOR.ITSID = PURCHASEINVOICE.SUPPLIER 
where REVERSEDLINEID is null :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

union all

select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
'SALES_TAX_PAYABLE' as ACC_CREDIT, TAX.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICETAXLINE.ITSTOTAL) as CREDIT
from SALESINVOICETAXLINE 
join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICETAXLINE.ITSOWNER
join TAX on TAX.ITSID = SALESINVOICETAXLINE.ITSTAX 
where TAX.DUEMETHOD = 0 :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

union all

select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
'SALES' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSTOTAL) as CREDIT
from SALESINVOICELINE
join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
where REVERSEDLINEID is null :WHEREADD
group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT


/*Jurnal entries all:*/
select *
from
  (
    select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
    'INVENTORY' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(PURCHASEINVOICELINE.ITSTOTAL) as DEBIT,
    null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
    from PURCHASEINVOICELINE
    join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICELINE.ITSOWNER
    join INVITEM on INVITEM.ITSID = PURCHASEINVOICELINE.INVITEM
    join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

    union all

    select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
    'SALES_TAX_RECIVABLE' as ACC_DEBIT, TAX.ITSNAME as SUBACC_DEBIT, sum(PURCHASEINVOICETAXLINE.ITSTOTAL) as DEBIT,
    null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
    from PURCHASEINVOICETAXLINE 
    join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICETAXLINE.ITSOWNER
    join TAX on TAX.ITSID = PURCHASEINVOICETAXLINE.ITSTAX 
    where TAX.DUEMETHOD = 0
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

    union all

    select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
    'ACC_RECIVABLE' as ACC_DEBIT, DEBTORCREDITOR.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.TOTALWITHTAXES ) as DEBIT,
    null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
    from SALESINVOICELINE
    join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
    join DEBTORCREDITOR on DEBTORCREDITOR.ITSID = SALESINVOICE.CUSTOMER 
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

    union all

    select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
    'COGSKNC' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as DEBIT,
    'INVENTORY' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as CREDIT
    from SALESINVOICELINE
    join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
    join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
    join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
    where INVITEM.KNOWNCOST is not null
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

    union all

    select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
    null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
    'ACC_PAYABLE' as ACC_CREDIT, DEBTORCREDITOR.ITSNAME as SUBACC_CREDIT, sum(PURCHASEINVOICELINE.TOTALWITHTAXES ) as CREDIT
    from PURCHASEINVOICELINE
    join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICELINE.ITSOWNER
    join DEBTORCREDITOR on DEBTORCREDITOR.ITSID = PURCHASEINVOICE.SUPPLIER 
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

    union all

    select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
    null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
    'SALES_TAX_PAYABLE' as ACC_CREDIT, TAX.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICETAXLINE.ITSTOTAL) as CREDIT
    from SALESINVOICETAXLINE 
    join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICETAXLINE.ITSOWNER
    join TAX on TAX.ITSID = SALESINVOICETAXLINE.ITSTAX 
    where TAX.DUEMETHOD = 0
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

    union all

    select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
    null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
    'SALES' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSTOTAL) as CREDIT
    from SALESINVOICELINE
    join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
    join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
    join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
    group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT
  ) as ENTRIES
order by ITSDATE, SOURCE_TYPE, SOURCE_ID;

/*Balance:*/
select ACCOUNT.ITSNUMBER, ACCOUNT.ITSNAME, SUBACC,
case when ACCOUNT.NORMALBALANCETYPE = 0 then ALL_RECORDS.DEBIT - ALL_RECORDS.CREDIT
else 0 end as DEBIT,
case when ACCOUNT.NORMALBALANCETYPE = 1 then ALL_RECORDS.CREDIT - ALL_RECORDS.DEBIT
else 0 end as CREDIT
from
  (
    select ACC, SUBACC, sum(DEBIT) as DEBIT, sum(CREDIT) as CREDIT
    from 
      (
        select ACC_DEBIT as ACC, SUBACC_DEBIT as SUBACC, DEBIT, 0.00 as CREDIT
        from 
          (
            select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
            'INVENTORY' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(PURCHASEINVOICELINE.ITSTOTAL) as DEBIT,
            null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
            from PURCHASEINVOICELINE
            join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICELINE.ITSOWNER
            join INVITEM on INVITEM.ITSID = PURCHASEINVOICELINE.INVITEM
            join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

            union all

            select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
            'SALES_TAX_RECIVABLE' as ACC_DEBIT, TAX.ITSNAME as SUBACC_DEBIT, sum(PURCHASEINVOICETAXLINE.ITSTOTAL) as DEBIT,
            null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
            from PURCHASEINVOICETAXLINE 
            join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICETAXLINE.ITSOWNER
            join TAX on TAX.ITSID = PURCHASEINVOICETAXLINE.ITSTAX 
            where TAX.DUEMETHOD = 0
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

            union all

            select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
            'ACC_RECIVABLE' as ACC_DEBIT, DEBTORCREDITOR.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.TOTALWITHTAXES ) as DEBIT,
            null as ACC_CREDIT, null as SUBACC_CREDIT, 0 as CREDIT
            from SALESINVOICELINE
            join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
            join DEBTORCREDITOR on DEBTORCREDITOR.ITSID = SALESINVOICE.CUSTOMER 
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

            union all

            select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
            'COGSKNC' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as DEBIT,
            'INVENTORY' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as CREDIT
            from SALESINVOICELINE
            join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
            join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
            join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
            where INVITEM.KNOWNCOST is not null
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT
          )
          
        union all
          
        select ACC_CREDIT as ACC, SUBACC_CREDIT as SUBACC, 0.00 as DEBIT, CREDIT
        from 
          (
            select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
            'COGSKNC' as ACC_DEBIT, INVITEMCATEGORY.ITSNAME as SUBACC_DEBIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as DEBIT,
            'INVENTORY' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSQUANTITY * INVITEM.KNOWNCOST) as CREDIT
            from SALESINVOICELINE
            join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
            join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
            join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
            where INVITEM.KNOWNCOST is not null
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

            union all

            select 1 as SOURCE_TYPE, PURCHASEINVOICE.ITSID as SOURCE_ID, PURCHASEINVOICE.ITSDATE, PURCHASEINVOICE.ORGANIZATION,
            null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
            'ACC_PAYABLE' as ACC_CREDIT, DEBTORCREDITOR.ITSNAME as SUBACC_CREDIT, sum(PURCHASEINVOICELINE.TOTALWITHTAXES ) as CREDIT
            from PURCHASEINVOICELINE
            join PURCHASEINVOICE on PURCHASEINVOICE.ITSID = PURCHASEINVOICELINE.ITSOWNER
            join DEBTORCREDITOR on DEBTORCREDITOR.ITSID = PURCHASEINVOICE.SUPPLIER 
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

            union all

            select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
            null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
            'SALES_TAX_PAYABLE' as ACC_CREDIT, TAX.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICETAXLINE.ITSTOTAL) as CREDIT
            from SALESINVOICETAXLINE 
            join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICETAXLINE.ITSOWNER
            join TAX on TAX.ITSID = SALESINVOICETAXLINE.ITSTAX 
            where TAX.DUEMETHOD = 0
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT

            union all

            select 2 as SOURCE_TYPE, SALESINVOICE.ITSID as SOURCE_ID, SALESINVOICE.ITSDATE, SALESINVOICE.ORGANIZATION,
            null as ACC_DEBIT, null as SUBACC_DEBIT, 0 as DEBIT,
            'SALES' as ACC_CREDIT, INVITEMCATEGORY.ITSNAME as SUBACC_CREDIT, sum(SALESINVOICELINE.ITSTOTAL) as CREDIT
            from SALESINVOICELINE
            join SALESINVOICE on SALESINVOICE.ITSID = SALESINVOICELINE.ITSOWNER
            join INVITEM on INVITEM.ITSID = SALESINVOICELINE.INVITEM
            join INVITEMCATEGORY on INVITEMCATEGORY.ITSID = INVITEM.ITSCATEGORY 
            group by SOURCE_TYPE, SOURCE_ID, ITSDATE, ORGANIZATION, ACC_DEBIT, SUBACC_DEBIT, ACC_CREDIT, SUBACC_CREDIT
          )
      )
    group by ACC, SUBACC
  ) as ALL_RECORDS
join ACCOUNT on ALL_RECORDS.ACC = ACCOUNT.ITSID
order by ACCOUNT.ITSNUMBER, SUBACC;
