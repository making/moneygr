namespace java moneygr.thrift

struct TUser {
    1: i32 userId;
    2: string email;
    4: string firstName;
    5: string lastName;
    6: TFamily family;
    7: list<TRole> roles;
}

struct TFamily {
    1: i32 familyId;
    2: string familyName;
}

struct TRole {
    1: string roleName;
}

service TUserService {
    list<TFamily> findFamilies();
    list<TUser> findUsers();
    TUser findUser(1: i32 userId);
    TUser create(1:TUser user, 2:string rawPassword);
    void deleteUser(1: i32 userId);
}