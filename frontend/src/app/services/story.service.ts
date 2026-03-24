import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StoryService {

private apiUrl = 'http://13.217.102.251:8080/api/story';

  constructor(private http: HttpClient) {}

  getStory(type: string): Observable<any> {   // ✅ MUST be getStory
    return this.http.get(`${this.apiUrl}?type=${type}`);
  }
}
