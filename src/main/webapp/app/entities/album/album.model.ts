import { BaseEntity } from './../../shared';

export const enum Visibility {
    'PRIVATE',
    'PUBLIC'
}

export class Album implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creationDate?: any,
        public visibility?: Visibility,
        public picures?: BaseEntity[],
    ) {
    }
}
