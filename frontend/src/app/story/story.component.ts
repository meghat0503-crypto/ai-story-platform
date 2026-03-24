import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StoryService } from '../services/story.service';

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, FormsModule],  // ✅ FIXED
  templateUrl: './story.component.html',
  styleUrls: ['./story.component.scss']
})
export class StoryComponent {

  selectedType = 'panchatantra';
  story = '';
  imageUrl = '';
  flipped = false;
  loading = false;

  constructor(private storyService: StoryService) {}

  generateStory() {
    this.loading = true;
    this.story = '';
this.imageUrl = '';

    this.storyService.getStory(this.selectedType).subscribe({
      next: (res: any) => {   // ✅ FIXED typing
	      this.story =res.story;
        this.imageUrl = res.imageUrl + '?t=' + new Date().getTime();
        this.flipped = !this.flipped;
        this.loading = false;
      },
      error: () => {
        this.story = 'Failed to load story';
        this.loading = false;
      }
    });
  }
}




























