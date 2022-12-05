select M.mID, M.mName, SUM(P.pPrice) 
from manufacturer M,part P
where M.mID=P.mID
Group by M.mID, M.mName
Order by SUM(P.pPrice) DESC