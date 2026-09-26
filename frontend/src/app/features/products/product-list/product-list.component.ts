import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';
import { Product } from '../../../core/models/product.model';
import { ProductService } from '../../../core/services/product.service';
import { ProductFormComponent } from '../product-form/product-form.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    DialogModule,
    ConfirmDialogModule,
    ProductFormComponent
  ],
  providers: [ConfirmationService],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  loading = false;
  saving = false;
  searchTerm = '';

  dialogVisible = false;
  editingProduct: Product | null = null;

  private search$ = new Subject<string>();
  private destroy$ = new Subject<void>();

  constructor(
    private productService: ProductService,
    private authService: AuthService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.search$
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((term) => this.productService.list(term || undefined)),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (products) => (this.products = products),
        error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar los productos' })
      });

    this.loadProducts();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.list(this.searchTerm || undefined).subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar los productos' });
      }
    });
  }

  onSearchChange(term: string): void {
    this.searchTerm = term;
    this.search$.next(term);
  }

  openCreateDialog(): void {
    this.editingProduct = null;
    this.dialogVisible = true;
  }

  openEditDialog(product: Product): void {
    this.editingProduct = { ...product };
    this.dialogVisible = true;
  }

  closeDialog(): void {
    this.dialogVisible = false;
    this.editingProduct = null;
  }

  handleSave(product: Product): void {
    this.saving = true;
    const request$ = product.id
      ? this.productService.update(product.id, product)
      : this.productService.create(product);

    request$.subscribe({
      next: () => {
        this.saving = false;
        this.closeDialog();
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: product.id ? 'Producto actualizado' : 'Producto agregado'
        });
        this.loadProducts();
      },
      error: () => {
        this.saving = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo guardar el producto' });
      }
    });
  }

  confirmDelete(product: Product): void {
    this.confirmationService.confirm({
      message: `¿Seguro que deseas eliminar "${product.name}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.deleteProduct(product)
    });
  }

  private deleteProduct(product: Product): void {
    if (!product.id) {
      return;
    }
    this.productService.delete(product.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Producto eliminado' });
        this.loadProducts();
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar el producto' })
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
