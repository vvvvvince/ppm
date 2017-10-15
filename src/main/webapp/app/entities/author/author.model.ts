import { BaseEntity } from './../../shared';

export class Author implements BaseEntity {
    constructor(
        public id?: number,
        public userName?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public pictures?: BaseEntity[],
        public comments?: BaseEntity[],
        public favorites?: BaseEntity[],
    ) {
    }
}
