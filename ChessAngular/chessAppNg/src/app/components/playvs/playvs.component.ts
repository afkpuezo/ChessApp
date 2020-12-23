import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from '@services/auth.service';
declare var myExtObject2: any;
@Component({
  selector: 'app-playvs',
  templateUrl: './playvs.component.html',
  styleUrls: ['./playvs.component.css']
})
export class PlayvsComponent implements OnInit {
  //const user = authService.userValue;
  
  
  title = 'chessAppNg';
  public un: String;
  public code = 0;
  public name = 0;
  public piece: String = './assets/img/chesspieces/wikipedia/{piece}.png'; 
  constructor() { }

  ngOnInit(): void{
    this.add()
    var v = localStorage.getItem('user');
    var myArr = JSON.parse(v);
    this.un = myArr["username"];
    //console.log(this.un)
    myExtObject2.setUsername(this.un);
    //{"id":4,"username":"t","email":"t@gmail.com","authdata":"dDp0"}
    
    // this.createGame('./assets/img/chesspieces/wikipedia/{piece}.png')
  }
  

  ngOnDestroy(){
    
    myExtObject2.delete();
  }

  test(){
    myExtObject2.test()
  }
  createGame(s: String,ori: String) {
    //console.log("hi")
    myExtObject2.setNull()
    myExtObject2.create(s,ori);
  }

  myFunction(){
    console.log("?")
  }

  callFunction2() {
      console.log(myExtObject2.func2());
  }

  delete(){
    myExtObject2.delete();
    
  }

  add(){
    myExtObject2.add();
  }

  getMoves(){
    myExtObject2.getMoves();
    console.log( myExtObject2.getMoves());
  }

  color(x: any){
    myExtObject2.color(x);
  }

  toggle(){
    myExtObject2.toggle();
  }

  generateCode(){
    var val = Math.floor(1000 + Math.random() * 100000);
    this.title = "w";
    this.code = val;
    myExtObject2.makeGame(this.code);
    this.createGame(this.piece,'white')
    
  }

  findGame(){
    myExtObject2.setCode(this.code)
    myExtObject2.findGame()
    this.createGame(this.piece,'black')
    myExtObject2.firstMove()
  }

  setPiece(s: String){
    this.piece = s;
  }

}
