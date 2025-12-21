$(function () {
  const slug = getCategorySlugFromPath(); // /categories/sneakers -> sneakers
  if (!slug) return;

  // 버튼들 기본 동작 연결
  $('#moreProductsBtn').on('click', function () {
    // 아직 전체 리스트 페이지가 없다면 일단 카테고리 URL 유지
    // 나중에 /categories/{slug}/products 같은 페이지 만들면 그쪽으로 연결
    location.href = '/categories/' + encodeURIComponent(slug);
  });

  // (선택) 인기순 버튼 - 아직 API 정렬 파라미터가 없으니 안내만
  $('#sortPopularBtn').on('click', function () {
    alert('인기순 정렬은 다음 단계에서 붙일게요!');
  });

  loadCategoryPage(slug);
});

function loadCategoryPage(slug) {
  $.ajax({
    url: '/api/categories/' + encodeURIComponent(slug),
    method: 'GET',
    success: function (res) {
      if (!res || res.success !== true) {
        renderError('데이터를 불러오지 못했습니다.');
        return;
      }

      const data = res.data;
      if (!data || !data.category) {
        renderError('카테고리 데이터가 없습니다.');
        return;
      }

      renderCategoryHeader(data.category);
      renderProducts(data.category, data.previewProducts || []);
      renderBrands(data.category, data.brands || []);
    },
    error: function (xhr) {
      const msg = xhr?.responseJSON?.error?.message || '서버 오류가 발생했습니다.';
      renderError(msg);
    }
  });
}

function renderCategoryHeader(category) {
  $('#categoryTitle').text(category.name);
  $('#categoryHeroImage').text(category.name);

  $('#productSectionTitle').text(category.name + ' 인기 상품');
  $('#brandSectionTitle').text(category.name + ' 브랜드');
}

function renderProducts(category, products) {
  const $grid = $('#productGrid');
  $grid.empty();

  if (!products.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">상품이 없습니다.</div>`);
    return;
  }

  products.forEach(p => {
    const thumb = p.thumbnailUrl
      ? `<img src="${escapeHtml(p.thumbnailUrl)}" alt="" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>NO IMAGE</span>`;

    const priceText = formatPrice(p.price);

    const cardHtml = `
      <div class="product-card" data-slug="${escapeHtml(p.slug)}">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-brand">${escapeHtml(p.brandName || '')}</div>
        <div class="product-name">${escapeHtml(p.name || '')}</div>
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

function renderBrands(category, brands) {
  const $grid = $('#brandGrid');
  $grid.empty();

  if (!brands.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">브랜드가 없습니다.</div>`);
    return;
  }

  brands.forEach(b => {
    const thumb = b.logoUrl
      ? `<img src="${escapeHtml(b.logoUrl)}" alt="" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>${escapeHtml(b.name || '')}</span>`;

    // 브랜드 카드 클릭 → (다음 단계) 카테고리 + 브랜드 필터
    // 지금은 필터 API가 없으니, 임시로 브랜드 페이지(있으면) or 검색으로 보내도 됨.
    const href = '/search?q=' + encodeURIComponent(b.name);

    const cardHtml = `
      <div class="product-card" data-brand-slug="${escapeHtml(b.slug)}">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-name">${escapeHtml(b.name || '')}</div>
        <div class="product-brand">바로가기</div>
      </div>
    `;

    const $card = $(cardHtml);
    $card.on('click', function () {
      location.href = href;
    });

    $grid.append($card);
  });
}

function renderError(message) {
  $('#productGrid').empty().append(`<div style="color:#888; font-size:13px;">${escapeHtml(message)}</div>`);
  $('#brandGrid').empty();
}

function getCategorySlugFromPath() {
  // 예: /categories/sneakers
  const path = window.location.pathname || '';
  const parts = path.split('/').filter(Boolean);
  // ["categories", "sneakers"]
  if (parts.length >= 2 && parts[0] === 'categories') return parts[1];
  return null;
}

function formatPrice(price) {
  // price가 BigDecimal이면 JSON에서 숫자/문자열로 올 수 있음
  const n = typeof price === 'number' ? price : Number(price);
  if (Number.isFinite(n)) return n.toLocaleString('ko-KR');
  return (price == null ? '' : String(price));
}

function escapeHtml(str) {
  if (str == null) return '';
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}
