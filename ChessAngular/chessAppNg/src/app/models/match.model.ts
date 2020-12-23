import { User } from '@models/user.model';
export class Match {
    id: number;
    code: number;
    whiteUser: User;
    blackUser: User;
    status: string;
    moveHistory: string;
    

    constructor(id: number, code: number, whiteUser: User, blackUser: User, status: string, moveHistory: string) {
        this.id = id;
        this.code = code;
        this.whiteUser = whiteUser;
        this.blackUser = blackUser;
        this.status = status;
        this.moveHistory = moveHistory;
    }
}