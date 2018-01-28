import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlidySharedModule } from '../../shared';
import { SlidyAdminModule } from '../../admin/admin.module';
import {
    SlideService,
    SlidePopupService,
    SlideComponent,
    SlideshowComponent,
    SlideDetailComponent,
    SlideDialogComponent,
    SlidePopupComponent,
    SlideDeletePopupComponent,
    SlideDeleteDialogComponent,
    slideRoute,
    slideshowRoute,
    slidePopupRoute,
} from './';

const ENTITY_STATES = [
    ...slideRoute,
    ...slideshowRoute,
    ...slidePopupRoute,
];

@NgModule({
    imports: [
        SlidySharedModule,
        SlidyAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SlideComponent,
        SlideshowComponent,
        SlideDetailComponent,
        SlideDialogComponent,
        SlideDeleteDialogComponent,
        SlidePopupComponent,
        SlideDeletePopupComponent,
    ],
    entryComponents: [
        SlideComponent,
        SlideDialogComponent,
        SlidePopupComponent,
        SlideDeleteDialogComponent,
        SlideDeletePopupComponent,
    ],
    providers: [
        SlideService,
        SlidePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlidySlideModule {}
