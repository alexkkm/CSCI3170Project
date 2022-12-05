select part.pID, 
part.pName, 
manufacturer.mName, 
category.cName,
part.pAvailableQuantity,
part.pWarrantyPeriod,
part.pPrice
from part, manufacturer, category
/*case search by manufacturer*/
where manufacturer.mName = 'Intel' /*change mName*/
/*case search by part name*/
/*where part.pName = ''*/
and part.cID = category.cID
and manufacturer.mID = part.mID
order by part.pPrice DESC; /*change SC*/