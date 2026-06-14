export default function ProductVisual({ product, compact = false }) {
    return (
        <div className={`product-visual ${compact ? "compact" : ""}`} style={{ "--tone": product.palette[0], "--ink": product.palette[1] }} role="img" aria-label={`${product.name} product illustration`}>
            <span className="visual-ring"></span>
            <span className="visual-object">{product.category.slice(0, 1)}</span>
            <span className="visual-label">{product.color}</span>
        </div>
    );
}
