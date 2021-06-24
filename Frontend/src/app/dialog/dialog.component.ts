import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  isOpen: boolean = false;

  public openDialog(): void{
    this.isOpen = true;
  }

  public closeDialog(): void{
    this.isOpen = false;
  }
}
