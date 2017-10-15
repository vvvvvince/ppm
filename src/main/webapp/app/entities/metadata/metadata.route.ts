import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MetadataComponent } from './metadata.component';
import { MetadataDetailComponent } from './metadata-detail.component';
import { MetadataPopupComponent } from './metadata-dialog.component';
import { MetadataDeletePopupComponent } from './metadata-delete-dialog.component';

export const metadataRoute: Routes = [
    {
        path: 'metadata',
        component: MetadataComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ppmApp.metadata.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'metadata/:id',
        component: MetadataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ppmApp.metadata.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const metadataPopupRoute: Routes = [
    {
        path: 'metadata-new',
        component: MetadataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ppmApp.metadata.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metadata/:id/edit',
        component: MetadataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ppmApp.metadata.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metadata/:id/delete',
        component: MetadataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ppmApp.metadata.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
