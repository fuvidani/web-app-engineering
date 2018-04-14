import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  answer: string = '';
  answerDisplay: string = '';
  showSpinner: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  showAnswer() {
    this.showSpinner = true;
    setTimeout(() => {
      this.answerDisplay = this.answer;
      this.showSpinner = false;
    }, 2000)
  }

}
