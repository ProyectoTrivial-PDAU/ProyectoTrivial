import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { A11yModule } from '@angular/cdk/a11y';

export interface QuestionCountDialogData {
  title: string;
  subtitle?: string;
  emoji?: string;
  initialCount?: number;
  min?: number;
  max?: number;
  playLabel?: string;
}

@Component({
  selector: 'app-question-count-dialog',
  standalone: true,
  imports: [CommonModule, A11yModule],
  templateUrl: './question-count-dialog.html',
  styleUrl: './question-count-dialog.scss'
})
export class QuestionCountDialog {
  questionCount: number;
  min: number;
  max: number;

  constructor(
    private dialogRef: DialogRef<number>,
    @Inject(DIALOG_DATA) public data: QuestionCountDialogData
  ) {
    this.min = data.min ?? 5;
    this.max = data.max ?? 20;
    this.questionCount = data.initialCount ?? 5;
  }

  increaseCount(): void {
    if (this.questionCount < this.max) {
      this.questionCount++;
    }
  }

  decreaseCount(): void {
    if (this.questionCount > this.min) {
      this.questionCount--;
    }
  }

  confirm(): void {
    this.dialogRef.close(this.questionCount);
  }

  cancel(): void {
    this.dialogRef.close(undefined);
  }
}
