select part.pID, 
part.pName, 
manufacturer.mName, 
category.cName,
part.pAvailableQuantity,
part.pWarrantyPeriod,
part.pPrice
from part, manufacturer, category
/*case search by manufacturer*/
where manufacturer.mName = %s /*change mName*/
/*case search by part name*/
/*where part.pName = %s*/
and part.cID = category.cID
and manufacturer.mID = part.mID
order by part.pPrice %s; /*change ASC*/

//




/* case search by Manufacture*/
select part.pID, part.pName, manufacturer.mName, category.cName, part.pAvailableQuantity, part.pWarrantyPeriod, part.pPrice
from part, manufacturer, category
where part.cID = category.cID
and manufacturer.mID = part.mID
and manufacturer.mName = %s
order by part.pPrice %s;

/* case search by part name */
select part.pID, part.pName, manufacturer.mName, category.cName, part.pAvailableQuantity, part.pWarrantyPeriod, part.pPrice
from part, manufacturer, category
where part.cID = category.cID
and manufacturer.mID = part.mID
and part.pName = %s
order by part.pPrice %s;