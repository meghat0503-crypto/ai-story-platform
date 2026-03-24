import { Component } from '@angular/core';
import { StoryComponent } from './story/story.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [StoryComponent],
  template: `<app-story></app-story>`
})
export class AppComponent {}
