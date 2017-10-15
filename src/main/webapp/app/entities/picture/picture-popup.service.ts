import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Picture } from './picture.model';
import { PictureService } from './picture.service';

@Injectable()
export class PicturePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private pictureService: PictureService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.pictureService.find(id).subscribe((picture) => {
                    if (picture.shootDate) {
                        picture.shootDate = {
                            year: picture.shootDate.getFullYear(),
                            month: picture.shootDate.getMonth() + 1,
                            day: picture.shootDate.getDate()
                        };
                    }
                    if (picture.postDate) {
                        picture.postDate = {
                            year: picture.postDate.getFullYear(),
                            month: picture.postDate.getMonth() + 1,
                            day: picture.postDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.pictureModalRef(component, picture);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.pictureModalRef(component, new Picture());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    pictureModalRef(component: Component, picture: Picture): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.picture = picture;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
