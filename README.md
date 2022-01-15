# InventoryTracker

Shopify Code Challenge

The code is adapted from [GalleryNeo](https://github.com/harrynull/galleryneo), last term's shopify challenge
(<del>by that I mean straight copy-pasting but hey that's where productivity comes from.</del>). I think this one is
easier than last one though, since it does not require session/user management and file management, so go check that one
out if you are interested.

It implements the following features:

1. RESTful endpoints for creating, reading, updating, deleting inventory items
    1. Support filter by out-of-stock or active items.
2. Export inventory to csv
3. Elegant, efficient and
   easy-to-maintain [code](https://github.com/harrynull/invtracker/blob/master/src/main/kotlin/tech/harrynull/invtracker/)
   .
4. Comprehensive [tests](https://github.com/harrynull/invtracker/blob/master/src/test/kotlin/tech/harrynull/invtracker/)
5. A quick and dirty frontend so you don't have to use command lines to test its functionality.
6. Hacked together in one day!

Written with Kotlin, Springboot, MySQL, protobuf

## Deployment/Development

You need to have an SQL database (tested with MySQL 8.0) installed and change the configuration
in `application.properties`. Alternatively, change your environment variables `MYSQL_HOST`, `MYSQL_USERNAME`
, `MYSQL_PASSWORD`, `MYSQL_DB`. The default values are `localhost:3306/inventory` and `localhost:3306/inventory_test`
with username and password as root

Then, you can run it with `./gradlew bootRun` and it will start a server
at [http://localhost:8080](http://localhost:8080)

You can also run the tests with `./gradlew test`

If you somehow really wants to use it in production, it is recommended to put it behind nginx and use the compiled jar
instead.

### Frontend

To run the frontend, run `cd frontend/ && npm install && npm run start` (or `yarn` if you prefer so)

Make sure it's running on [http://localhost:3000](http://localhost:3000) or CORS won't be working.

Spoiler: the code for FE is hacky and dirty but, hey, it works.

## License

This program is licensed under [AGPLv3](https://github.com/harrynull/GalleryNeo/blob/master/LICENSE). That said, you are
discouraged from using it for your own code challenge.
