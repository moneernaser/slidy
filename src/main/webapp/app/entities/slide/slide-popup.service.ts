import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Slide } from './slide.model';
import { SlideService } from './slide.service';

@Injectable()
export class SlidePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private slideService: SlideService

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
                this.slideService.find(id).subscribe((slide) => {
                    slide.showStartDate = this.datePipe
                        .transform(slide.showStartDate, 'yyyy-MM-ddTHH:mm:ss');
                    slide.showEndDate = this.datePipe
                        .transform(slide.showEndDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.slideModalRef(component, slide);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.slideModalRef(component, new Slide());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    slideModalRef(component: Component, slide: Slide): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.slide = slide;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
