import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  
    title = 'Welcome to the Chess Club!';

  constructor() { }

  ngOnInit(): void {
  }
}
