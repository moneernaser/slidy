import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Slide } from './slide.model';
import { SlideService } from './slide.service';
import { Principal, ResponseWrapper } from '../../shared';
import {log} from 'util';

@Component({
    selector: 'jhi-slide',
    template: '<div></div>'
})
export class SlideshowComponent implements OnInit {
slides: Slide[];
    currentAccount: any;
    // eventSubscriber: Subscription;

    constructor(
        private slideService: SlideService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.slideService.query().subscribe(
            (res: ResponseWrapper) => {
                this.slides = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            window.open('/slideshow/', '_blank');

        });
    }

    trackId(index: number, item: Slide) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    // gotoSlideshowPage() {
    //     window.location.href = '/hellooo';
    // }

    // openFile(contentType, field) {
    //     return this.dataUtils.openFile(contentType, field);
    // }
    // registerChangeInSlides() {
    //     this.eventSubscriber = this.eventManager.subscribe('slideListModification', (response) => this.loadAll());
    // }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
