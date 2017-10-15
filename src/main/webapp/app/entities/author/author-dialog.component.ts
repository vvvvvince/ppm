import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Author } from './author.model';
import { AuthorPopupService } from './author-popup.service';
import { AuthorService } from './author.service';
import { Picture, PictureService } from '../picture';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-author-dialog',
    templateUrl: './author-dialog.component.html'
})
export class AuthorDialogComponent implements OnInit {

    author: Author;
    isSaving: boolean;

    pictures: Picture[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private authorService: AuthorService,
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
        if (this.author.id !== undefined) {
            this.subscribeToSaveResponse(
                this.authorService.update(this.author));
        } else {
            this.subscribeToSaveResponse(
                this.authorService.create(this.author));
        }
    }

    private subscribeToSaveResponse(result: Observable<Author>) {
        result.subscribe((res: Author) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Author) {
        this.eventManager.broadcast({ name: 'authorListModification', content: 'OK'});
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
    selector: 'jhi-author-popup',
    template: ''
})
export class AuthorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private authorPopupService: AuthorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.authorPopupService
                    .open(AuthorDialogComponent as Component, params['id']);
            } else {
                this.authorPopupService
                    .open(AuthorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
