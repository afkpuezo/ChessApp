import { Component, OnInit, OnDestroy } from '@angular/core';
declare var myExtObject: any;
@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit, OnDestroy{
  title = 'chessAppNg';
  ngOnInit(): void{
    this.add()
    this.createGame('./assets/img/chesspieces/wikipedia/{piece}.png')
  }

  ngOnDestroy(){
    myExtObject.delete(1);
  }
  createGame(s: String) {
    console.log("hi")
    myExtObject.create(s);
  }

  myFunction(){
    console.log("?")
  }

  callFunction2() {
      console.log(myExtObject.func2());
  }

  delete(){
    myExtObject.delete(1);
    
  }

  add(){
    myExtObject.add(1);
  }

  getMoves(){
    myExtObject.getMoves();
    console.log( myExtObject.getMoves());
  }

  color(x: any){
    myExtObject.color(x);
  }

  toggle(){
    myExtObject.toggle();
  }

}
