print('initializing database with default research facilities');


db.abstract_user.save(
    {
        "_id": ObjectId("5ac9e3e48a6d874b3da4b3f9"),
        "email": "research@who.com",
        "password": "$2a$15$8lEAUlhZagj4Egwt87Vq5ect2LBCwIzwHVFgFTugzKLS/jvebdiGu",
        "_class": "at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility"
    }
);

db.abstract_user.save(
    {
        "_id": ObjectId("5ac9e3e48a6d874b3da4b3ff"),
        "email": "research@bayer.com",
        "password": "$2a$15$8lEAUlhZagj4Egwt87Vq5ect2LBCwIzwHVFgFTugzKLS/jvebdiGu",
        "_class": "at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility"
    }
);

print('initialization of research facilities completed');