export class User {
    id: number;
    username: string;
    barePassword: string;
    email: string;
    authdata?: string;
    

    constructor(id: number, username: string, barePassword: string, email: string) {
        this.id = id;
        this.username = username;
        this.barePassword = barePassword;
        this.email = email;
    }
}