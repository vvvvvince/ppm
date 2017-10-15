import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Picture } from './picture.model';
import { PicturePopupService } from './picture-popup.service';
import { PictureService } from './picture.service';
import { Album, AlbumService } from '../album';
import { Author, AuthorService } from '../author';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-picture-dialog',
    templateUrl: './picture-dialog.component.html'
})
export class PictureDialogComponent implements OnInit {

    picture: Picture;
    isSaving: boolean;

    raws: Picture[];

    albums: Album[];

    authors: Author[];
    shootDateDp: any;
    postDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private pictureService: PictureService,
        private albumService: AlbumService,
        private authorService: AuthorService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pictureService
            .query({filter: 'picture-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.picture.rawId) {
                    this.raws = res.json;
                } else {
                    this.pictureService
                        .find(this.picture.rawId)
                        .subscribe((subRes: Picture) => {
                            this.raws = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.albumService.query()
            .subscribe((res: ResponseWrapper) => { this.albums = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.authorService.query()
            .subscribe((res: ResponseWrapper) => { this.authors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.picture, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.picture.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pictureService.update(this.picture));
        } else {
            this.subscribeToSaveResponse(
                this.pictureService.create(this.picture));
        }
    }

    private subscribeToSaveResponse(result: Observable<Picture>) {
        result.subscribe((res: Picture) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Picture) {
        this.eventManager.broadcast({ name: 'pictureListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPictureById(index: number, item: Picture) {
        return item.id;
    }

    trackAlbumById(index: number, item: Album) {
        return item.id;
    }

    trackAuthorById(index: number, item: Author) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-picture-popup',
    template: ''
})
export class PicturePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private picturePopupService: PicturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.picturePopupService
                    .open(PictureDialogComponent as Component, params['id']);
            } else {
                this.picturePopupService
                    .open(PictureDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
