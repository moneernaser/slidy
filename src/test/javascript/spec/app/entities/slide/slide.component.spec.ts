/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SlidyTestModule } from '../../../test.module';
import { SlideComponent } from '../../../../../../main/webapp/app/entities/slide/slide.component';
import { SlideService } from '../../../../../../main/webapp/app/entities/slide/slide.service';
import { Slide } from '../../../../../../main/webapp/app/entities/slide/slide.model';

describe('Component Tests', () => {

    describe('Slide Management Component', () => {
        let comp: SlideComponent;
        let fixture: ComponentFixture<SlideComponent>;
        let service: SlideService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlidyTestModule],
                declarations: [SlideComponent],
                providers: [
                    SlideService
                ]
            })
            .overrideTemplate(SlideComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SlideComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SlideService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Slide(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.slides[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
