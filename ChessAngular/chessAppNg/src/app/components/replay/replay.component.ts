import { Component, OnInit, OnDestroy } from '@angular/core';
declare var myExtObject: any;
@Component({
  selector: 'app-replay',
  templateUrl: './replay.component.html',
  styleUrls: ['./replay.component.css']
})
export class ReplayComponent implements OnInit, OnDestroy{
  title = 'chessAppNg';
  public code = 0;
  public un: String;
  ngOnInit(): void{
    var v = localStorage.getItem('user');
    var myArr = JSON.parse(v);
    this.un = myArr["username"];
    this.add()
   
  }

  ngOnDestroy(){
    myExtObject.delete(2);
  }
  createGame(s: String) {
    console.log("hi")
    myExtObject.create2(s);
  }

  myFunction(){
    console.log("?")
  }
  findGame(){
    myExtObject.find(this.code,this.un)
    this.createGame('./assets/img/chesspieces/wikipedia/{piece}.png')
  }

  next(){
    myExtObject.next()
  }

  prev(){
    myExtObject.prev()
  }

  callFunction2() {
      console.log(myExtObject.func2());
  }

  delete(){
    myExtObject.delete();
    
  }

  add(){
    myExtObject.add(2);
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
