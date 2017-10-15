import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Metadata } from './metadata.model';
import { MetadataPopupService } from './metadata-popup.service';
import { MetadataService } from './metadata.service';
import { Picture, PictureService } from '../picture';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-metadata-dialog',
    templateUrl: './metadata-dialog.component.html'
})
export class MetadataDialogComponent implements OnInit {

    metadata: Metadata;
    isSaving: boolean;

    pictures: Picture[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private metadataService: MetadataService,
        private pictureService: PictureService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pictureService.query()
            .subscribe((res: ResponseWrapper) => { this.pictures = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.metadata.id !== undefined) {
            this.subscribeToSaveResponse(
                this.metadataService.update(this.metadata));
        } else {
            this.subscribeToSaveResponse(
                this.metadataService.create(this.metadata));
        }
    }

    private subscribeToSaveResponse(result: Observable<Metadata>) {
        result.subscribe((res: Metadata) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Metadata) {
        this.eventManager.broadcast({ name: 'metadataListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-metadata-popup',
    template: ''
})
export class MetadataPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metadataPopupService: MetadataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.metadataPopupService
                    .open(MetadataDialogComponent as Component, params['id']);
            } else {
                this.metadataPopupService
                    .open(MetadataDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
