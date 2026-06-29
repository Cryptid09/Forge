export function Loader({ label = 'Loading...' }: { label?: string }) {
  return (
    <div className="flex min-h-55 items-center justify-center rounded-2xl border border-slate-200 bg-white/80 p-6 shadow-sm">
      <div className="flex flex-col items-center gap-4 text-center">
        <div className="flex h-20 w-20 items-center justify-center rounded-2xl bg-slate-50 shadow-inner ring-1 ring-slate-200">
          <img
            src="/Forge-logo.png"
            alt="Forge"
            className="h-14 w-14 animate-pulse object-contain"
          />
        </div>
        <div className="space-y-1">
          <div className="mx-auto h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-blue-600" />
          <p className="text-sm font-medium text-slate-700">{label}</p>
        </div>
      </div>
    </div>
  )
}