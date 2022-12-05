select part.pName, 
part.pAvailableQuantity
from part, manufacturer, category
where part.pID = %d  /*change pID*/
and salesperson.sID = %d  /*change sID*/


select part.pName, part.pAvailableQuantity
from part, manufacturer, category
where part.pID = %d  /*change pID*/
and salesperson.sID = %d  /*change sID*/