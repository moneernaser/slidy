import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Slide } from './slide.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SlideService {

    private resourceUrl =  SERVER_API_URL + 'api/slides';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(slide: Slide): Observable<Slide> {
        const copy = this.convert(slide);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(slide: Slide): Observable<Slide> {
        const copy = this.convert(slide);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Slide> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        console.log(result);
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Slide.
     */
    private convertItemFromServer(json: any): Slide {
        const entity: Slide = Object.assign(new Slide(), json);
        entity.showStartDate = this.dateUtils
            .convertDateTimeFromServer(json.showStartDate);
        entity.showEndDate = this.dateUtils
            .convertDateTimeFromServer(json.showEndDate);
        return entity;
    }

    /**
     * Convert a Slide to a JSON which can be sent to the server.
     */
    private convert(slide: Slide): Slide {
        const copy: Slide = Object.assign({}, slide);

        copy.showStartDate = this.dateUtils.toDate(slide.showStartDate);

        copy.showEndDate = this.dateUtils.toDate(slide.showEndDate);
        return copy;
    }
}
