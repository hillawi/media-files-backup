import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesDetailsComponent } from './files-details.component';

describe('FilesDetailsComponent', () => {
  let component: FilesDetailsComponent;
  let fixture: ComponentFixture<FilesDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FilesDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilesDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
