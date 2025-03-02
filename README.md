### Railway

Bring your code, we'll handle the rest. Made for any language, for projects big and
small. [Railway](https://railway.app/)
is the cloud that takes the complexity out of shipping software.

Create a new empty project in Railway and start by creating a PostgreSQL database. Once you have that created you can
create
a new project from GitHub. You can use the following environment variables based on the database you just created.

```properties
spring_profiles_active=prod
PROD_DB_HOST=HOST_HERE
PROD_DB_PORT=POST_HERE
PROD_DB_NAME=railway
PROD_DB_PASSWORD=PASSWORD_HERE
PROD_DB_USERNAME=postgres
```

You don't need GitHub Actions or any type of pipeline for this setup because Railway handles this for you. Simply push
your code to GitHub
and a new build and deploy will be triggered. If you want to disable this functionality you can from the settings of
your project
on Railway. 
