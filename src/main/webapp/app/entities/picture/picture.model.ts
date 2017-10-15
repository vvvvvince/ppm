import { BaseEntity } from './../../shared';

export const enum PictureType {
    'JPG',
    'PNG',
    'RAW'
}

export const enum Visibility {
    'PRIVATE',
    'PUBLIC'
}

export class Picture implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public dataContentType?: string,
        public data?: any,
        public type?: PictureType,
        public visibility?: Visibility,
        public shootDate?: any,
        public postDate?: any,
        public rawId?: number,
        public comments?: BaseEntity[],
        public metadata?: BaseEntity[],
        public albums?: BaseEntity[],
        public authorId?: number,
        public likers?: BaseEntity[],
    ) {
    }
}
