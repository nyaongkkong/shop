$(function () {

  const slug = getBrandSlug();
  if (!slug) return;

  $.ajax({
    url: '/api/brands/' + encodeURIComponent(slug),
    method: 'GET',
    success: function (res) {

      if (!res || res.success !== true) {
        renderError('브랜드 정보를 불러오지 못했습니다.');
        return;
      }

      renderBrand(res.data.brand);
      renderProducts(res.data.products || []);
    },
    error: function (xhr) {
      console.log('brand api error:', xhr.status, xhr.responseText);
      renderError(xhr?.responseJSON?.error?.message || '서버 오류');
    }
  });

});

function renderBrand(brand) {
  $('#brandTitle').text(brand.name);

  if (brand.logoUrl) {
    $('#brandHeroImage').html(
      `<img src="${escapeHtml(brand.logoUrl)}"
            style="width:100%; height:100%; object-fit:cover;">`
    );
  }
}

function renderProducts(products) {
  const $grid = $('#productGrid');
  $grid.empty();

  if (!products.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">상품이 없습니다.</div>`);
    return;
  }

  products.forEach(p => {

    const thumb = p.thumbnailUrl
      ? `<img src="${escapeHtml(p.thumbnailUrl)}" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>NO IMAGE</span>`;

    const priceText = formatPrice(p.price);

    const cardHtml = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-name">${escapeHtml(p.name)}</div>
        <div class="product-price">
          <span>${priceText}</span>
          <span class="product-price-label">원</span>
        </div>
      </div>
    `;

    const $card = $(cardHtml);
    $card.on('click', function () {
      location.href = '/products/' + encodeURIComponent(p.slug);
    });

    $grid.append($card);
  });
}

function getBrandSlug() {
  const parts = location.pathname.split('/').filter(Boolean);
  // /brands/{slug}
  return parts.length === 2 ? parts[1] : null;
}

function renderError(msg) {
  $('#productGrid').html(`<div style="color:#888;">${escapeHtml(msg)}</div>`);
}

function formatPrice(price) {
  const n = typeof price === 'number' ? price : Number(price);
  return Number.isFinite(n) ? n.toLocaleString('ko-KR') : price;
}

function escapeHtml(str) {
  if (!str) return '';
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}