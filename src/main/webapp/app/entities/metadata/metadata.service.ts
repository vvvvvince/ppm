import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Metadata } from './metadata.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MetadataService {

    private resourceUrl = SERVER_API_URL + 'api/metadata';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/metadata';

    constructor(private http: Http) { }

    create(metadata: Metadata): Observable<Metadata> {
        const copy = this.convert(metadata);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(metadata: Metadata): Observable<Metadata> {
        const copy = this.convert(metadata);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Metadata> {
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

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Metadata.
     */
    private convertItemFromServer(json: any): Metadata {
        const entity: Metadata = Object.assign(new Metadata(), json);
        return entity;
    }

    /**
     * Convert a Metadata to a JSON which can be sent to the server.
     */
    private convert(metadata: Metadata): Metadata {
        const copy: Metadata = Object.assign({}, metadata);
        return copy;
    }
}
