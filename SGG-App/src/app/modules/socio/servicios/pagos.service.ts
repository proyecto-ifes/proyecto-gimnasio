import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PagosService {
  ip = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }
  
  httpOptions = {
    headers: new HttpHeaders({
      'Accept':  'application/json;profile=urn:org.apache.isis/v1',
      'Authorization': 'Basic aXNpcy1tb2R1bGUtc2VjdXJpdHktYWRtaW46cGFzcw==',
    })
  }

  private Url = this.ip+'/restful/objects/gimnasio.socios/';


  getPagos(id: number){
     return this.httpClient.get(this.Url+id+'/collections/pagos', this.httpOptions);
  }

}
