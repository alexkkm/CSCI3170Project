select part.pName, 
part.pAvailableQuantity
from part, manufacturer, category
where part.pID = 1  /*change pID*/
and salesperson.sID = 1  /*change sID*/