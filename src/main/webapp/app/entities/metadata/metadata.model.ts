import { BaseEntity } from './../../shared';

export const enum MetadataType {
    'TAG',
    'EXIF'
}

export class Metadata implements BaseEntity {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
        public type?: MetadataType,
        public pictureId?: number,
    ) {
    }
}
