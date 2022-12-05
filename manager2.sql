select S.sID,S.sName,S.sExperience,Count(*) as "Number of Transaction"
from salesperson S, transaction T
where S.sID=T.sID AND S.sExperience>=1 AND S.sExperience<=3
/*
need to be modifyed (sExperience)
*/
GROUP BY S.sID,S.sName,S.sExperience
HAVING COUNT(*) > 1
order by S.sID DESC
