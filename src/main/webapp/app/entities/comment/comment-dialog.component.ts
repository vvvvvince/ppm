import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Comment } from './comment.model';
import { CommentPopupService } from './comment-popup.service';
import { CommentService } from './comment.service';
import { Author, AuthorService } from '../author';
import { Picture, PictureService } from '../picture';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-comment-dialog',
    templateUrl: './comment-dialog.component.html'
})
export class CommentDialogComponent implements OnInit {

    comment: Comment;
    isSaving: boolean;

    authors: Author[];

    pictures: Picture[];
    postDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private commentService: CommentService,
        private authorService: AuthorService,
        private pictureService: PictureService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorService.query()
            .subscribe((res: ResponseWrapper) => { this.authors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.pictureService.query()
            .subscribe((res: ResponseWrapper) => { this.pictures = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.comment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.commentService.update(this.comment));
        } else {
            this.subscribeToSaveResponse(
                this.commentService.create(this.comment));
        }
    }

    private subscribeToSaveResponse(result: Observable<Comment>) {
        result.subscribe((res: Comment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Comment) {
        this.eventManager.broadcast({ name: 'commentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAuthorById(index: number, item: Author) {
        return item.id;
    }

    trackPictureById(index: number, item: Picture) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-comment-popup',
    template: ''
})
export class CommentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commentPopupService: CommentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.commentPopupService
                    .open(CommentDialogComponent as Component, params['id']);
            } else {
                this.commentPopupService
                    .open(CommentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
