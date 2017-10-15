import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Metadata } from './metadata.model';
import { MetadataPopupService } from './metadata-popup.service';
import { MetadataService } from './metadata.service';

@Component({
    selector: 'jhi-metadata-delete-dialog',
    templateUrl: './metadata-delete-dialog.component.html'
})
export class MetadataDeleteDialogComponent {

    metadata: Metadata;

    constructor(
        private metadataService: MetadataService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.metadataService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'metadataListModification',
                content: 'Deleted an metadata'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-metadata-delete-popup',
    template: ''
})
export class MetadataDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metadataPopupService: MetadataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.metadataPopupService
                .open(MetadataDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
