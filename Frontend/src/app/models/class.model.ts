import {User} from "./user.model";
import {Subject} from "./subject.model";

export interface Class {
  classId: number;
  className: string;
  user: number[];
  subjects: number[];

  /*
  user: User[];
  subjects: Subject[];
   */

}
