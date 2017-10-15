import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PpmMetadataModule } from './metadata/metadata.module';
import { PpmAlbumModule } from './album/album.module';
import { PpmCommentModule } from './comment/comment.module';
import { PpmPictureModule } from './picture/picture.module';
import { PpmAuthorModule } from './author/author.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PpmMetadataModule,
        PpmAlbumModule,
        PpmCommentModule,
        PpmPictureModule,
        PpmAuthorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PpmEntityModule {}
