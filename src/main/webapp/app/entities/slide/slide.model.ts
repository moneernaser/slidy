import { BaseEntity, User } from './../../shared';

export class Slide implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public dataContentType?: string,
        public data?: any,
        public showStartDate?: any,
        public showEndDate?: any,
        public user?: User,
    ) {
    }
}
