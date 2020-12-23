import { User } from "./user.model";

export class Game {
    id: number;
    code: number;
    whiteUser: User

    constructor(id: number, code: number, whiteUser: User) {
        this.id = id;
        this.code = code;
        this.whiteUser = whiteUser;   
    }

}
