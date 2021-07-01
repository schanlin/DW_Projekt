import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import {faTimes} from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit {

  @Input() headline = '';
  @Output() openChange = new EventEmitter<boolean>();
  readonly faTimes = faTimes;

  constructor() { }

  ngOnInit(): void {
  }

  set isOpen(value: boolean){
    this.openChange.emit(value);
    this._isOpen = value;
  }
  get isOpen(): boolean{
    return this._isOpen;
  }
  private _isOpen: boolean = false;


  //isOpen: boolean = false;

  public openDialog(): void{
    this.isOpen = true;
    //this.openChange.emit(true);
  }

  public closeDialog(): void{
    this.isOpen = false;
    //this.openChange.emit(false);
  }
}
