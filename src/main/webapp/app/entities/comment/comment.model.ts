import { BaseEntity } from './../../shared';

export const enum Visibility {
    'PRIVATE',
    'PUBLIC'
}

export class Comment implements BaseEntity {
    constructor(
        public id?: number,
        public content?: string,
        public postDate?: any,
        public visibility?: Visibility,
        public authorId?: number,
        public pictureId?: number,
    ) {
    }
}
