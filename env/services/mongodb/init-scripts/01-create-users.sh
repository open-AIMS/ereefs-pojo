mongo admin --eval "db.createUser({user: 'root', pwd: 'pwd', roles: [{role: 'root', db: 'admin'}]});"
mongo admin --eval "db.createUser({user: 'pojo', pwd: 'pojo', roles: [{role: 'readWrite', db: '*'}]});"
