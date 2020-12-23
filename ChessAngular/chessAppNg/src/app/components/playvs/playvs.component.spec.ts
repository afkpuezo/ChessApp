import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayvsComponent } from './playvs.component';

describe('PlayvsComponent', () => {
  let component: PlayvsComponent;
  let fixture: ComponentFixture<PlayvsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlayvsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayvsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
